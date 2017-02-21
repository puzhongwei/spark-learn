package com;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class Algorithms
{
	public static <T> List<List<T>> combine( T[] a, int m )
	{
		int length = a.length;
		if ( m > length || m < 1 )
		{
			throw new RuntimeException( "unvalid parameter m: " + m );
		}

		List result = Lists.newArrayList();
		if( m == length )
		{
			result.add( Arrays.asList( a ) );
			return result;
		}

		byte[] checked = new byte[length];
		for ( int i = 0; i < length; i++ )
		{
			if ( i < m )
			{
				checked[i] = 1;
			}
			else
			{
				checked[i] = 0;
			}
		}

		boolean flag = true;
		boolean tempFlag = false;
		int pos = 0;
		int sum = 0;
		do
		{
			sum = 0;
			pos = 0;
			tempFlag = true;
			result.add( print( checked, a, m ) );

			for ( int i = 0; i < length - 1; i++ )
			{
				if ( checked[i] == 1 && checked[i + 1] == 0 )
				{
					checked[i] = 0;
					checked[i + 1] = 1;
					pos = i;
					break;
				}
			}

			for ( int i = 0; i < pos; i++ )
			{
				if ( checked[i] == 1 )
				{
					sum++;
				}
			}
			for ( int i = 0; i < pos; i++ )
			{
				if ( i < sum )
				{
					checked[i] = 1;
				}
				else
				{
					checked[i] = 0;
				}
			}

			for ( int i = length - m; i < length; i++ )
			{
				if ( checked[i] == 0 )
				{
					tempFlag = false;
					break;
				}
			}
			flag = !tempFlag;
		} while ( flag );

		result.add( print( checked, a, m ) );

		return result;
	}

	private static <T> List<T> print( byte[] checked, T[] a, int m )
	{
		List<T> result = Lists.newArrayListWithExpectedSize( m );
		for ( int i = 0; i < checked.length; i++ )
		{
			if ( checked[i] == 1 )
			{
				result.add( a[i] );
			}
		}
		return result;
	}
}

